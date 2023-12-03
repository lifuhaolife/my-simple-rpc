package com.lfh.rpc.server.nameservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lfh.rpc.api.NameService;
import com.lfh.rpc.server.seriaize.SerializeSupport;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/11/30 21:27
 */
public class LocalFileNameService implements NameService {

    private final Logger logger = LoggerFactory.getLogger(LocalFileNameService.class);

    private static final Collection<String> schemes = Collections.singleton("file");
    ;
    private File file;


    private static final String NAME_SERVICE_META_DATA_STORAGE_PATH =
            System.getProperty("java.io.tmpdir") + File.separator + "my_rpc_name_service.data";

    @Override
    public URI getNameServiceUrl() {
        return new File(NAME_SERVICE_META_DATA_STORAGE_PATH).toURI();
    }

    @Override
    public Collection<String> supportSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServiceUri) {
        if (schemes.contains(nameServiceUri.getScheme())) {
            file = new File(nameServiceUri);
        } else {
            throw new RuntimeException("unsupported scheme!");
        }
    }

    @Override
    public synchronized void registerService(String serviceName, URI serviceAddress) throws IOException {
        logger.info("Register service: {}, uri : {}", serviceName, serviceAddress);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                int fileLength = (int) raf.length();
                MetaData metaData;
                byte[] bytes;
                if (fileLength > 0) {
                    bytes = new byte[fileLength];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) {
                        fileChannel.read(buffer);
                    }
                    metaData = SerializeSupport.parse(bytes);
                } else {
                    metaData = new MetaData();
                }
                List<URI> uris = metaData.computeIfAbsent(serviceName, k -> new ArrayList<>());
                if (!uris.contains(serviceAddress)) {
                    uris.add(serviceAddress);
                }
                String jsonMetaData = JSON.toJSONString(metaData);
                logger.info(jsonMetaData);
                //写入磁盘
                fileChannel.truncate(jsonMetaData.getBytes(StandardCharsets.UTF_8).length);
                fileChannel.position(0);
                fileChannel.write(ByteBuffer.wrap(SerializeSupport.serialize(metaData)));
                fileChannel.force(true);
            } finally {
                lock.release();
            }

        }
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        MetaData metaData = new MetaData();
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                byte[] bytes = new byte[(int) raf.length()];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining()) {
                    fileChannel.read(buffer);
                }
                if (bytes.length != 0) {
                    metaData = SerializeSupport.parse(bytes);
                }
            } finally {
                lock.release();
            }
        }
        List<URI> uris = metaData.get(serviceName);
        if (null == uris || uris.isEmpty()) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(uris.size());
        return uris.get(index);
    }

}
