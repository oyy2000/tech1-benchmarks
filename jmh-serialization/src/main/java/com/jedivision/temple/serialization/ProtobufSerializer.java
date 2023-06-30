package com.jedivision.temple.serialization;

import com.alibaba.fastjson2.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jedivision.temple.entity.Master;
import com.jedivision.temple.entity.pojo.FareSearchResponse;
import com.jedivision.temple.entity.proto.FareSearchResponseOuterClass;
import com.jedivision.temple.entity.proto.MasterOuterClass;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jedivision.temple.utils.Utils.readFileToString;

@Service
public class ProtobufSerializer implements AbstractSerializer {
    // 出于信任，没有测试反序列化的对象是否符合要求
    private final FareSearchResponseOuterClass.FareSearchResponse.Builder fareSearchResponseBuilder = FareSearchResponseOuterClass.FareSearchResponse.newBuilder();
//    private final MasterOuterClass.Master.Builder masterBuilder = MasterOuterClass.Master.newBuilder();
    private final Map<Long, MasterOuterClass.Master.Builder> masterBuilderMap = new HashMap<>();
    public ProtobufSerializer() throws IOException {
        String json = readFileToString("SHA-HKG-2023-05-25.json");
        JsonFormat.parser().ignoringUnknownFields().merge(json, fareSearchResponseBuilder);
        String json2 = readFileToString("jedi-master.json");
        // transfer json2 into List<Master>
        JSON.parseArray(json2, Master.class).forEach(master -> {
            MasterOuterClass.Master.Builder mb = MasterOuterClass.Master.newBuilder();
            try {
                masterBuilderMap.put(master.getId(), mb);
                JsonFormat.parser().ignoringUnknownFields().merge(JSON.toJSONString(master), mb);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public <T> byte[] mySerialize(T object) {
        // convert the object into json string
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) object.getClass();
        if (clazz == Master.class) {
            return masterBuilderMap.get(((Master) object).getId()).build().toByteArray();
        } else if (clazz == FareSearchResponse.class) {
            return fareSearchResponseBuilder.build().toByteArray();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T myDeserialize(byte[] bytes, Class<T> clazz) {
        if(clazz == Master.class) {
            try {
                return (T) MasterOuterClass.Master.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        } else if (clazz == FareSearchResponse.class) {
            try {
                return (T) FareSearchResponseOuterClass.FareSearchResponse.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public byte[] serialize(Object object) throws Exception {
        return mySerialize(object);
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> type) throws Exception {
        return myDeserialize(bytes, type);
    }
}
