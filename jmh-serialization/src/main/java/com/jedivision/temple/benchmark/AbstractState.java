package com.jedivision.temple.benchmark;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jedivision.temple.configuration.Application;
import com.jedivision.temple.entity.pojo.FareSearchResponse;
import com.jedivision.temple.serialization.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jedivision.temple.serialization.SerializationType.*;
import static com.jedivision.temple.utils.Utils.readFileToString;

public class AbstractState {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss X"));

    private final ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
    private final JdkSerializer jdkSerializer = context.getBean(JdkSerializer.class);
    private final FastjsonSerializer fastjsonSerializer = context.getBean(FastjsonSerializer.class);
    private final Fastjson2Serializer fastjson2Serializer = context.getBean(Fastjson2Serializer.class);
    private final JacksonJsonSerializer jacksonJsonSerializer = context.getBean(JacksonJsonSerializer.class);
    private final JacksonSmileSerializer jacksonSmileSerializer = context.getBean(JacksonSmileSerializer.class);
    private final FstSerializer fstSerializer = context.getBean(FstSerializer.class);
    private final FstUnsafeSerializer fstUnsafeSerializer = context.getBean(FstUnsafeSerializer.class);
    private final KryoSerializer kryoSerializer = context.getBean(KryoSerializer.class);
    private final MessagePackSerializer messagePackSerializer = context.getBean(MessagePackSerializer.class);

    private final ProtoStuffSerializer protoStuffSerializer = context.getBean(ProtoStuffSerializer.class);
    private final ProtobufSerializer protobufSerializer = context.getBean(ProtobufSerializer.class);

    private final Map<SerializationType, AbstractSerializer> serializers = new HashMap<>();
    private Class<?> userClass;
    private List<Serializable> users = Collections.emptyList();
    private final Map<SerializationType, List<byte[]>> usersBytesBySerializationType = new HashMap<>();

    protected void initSerializers() {
        serializers.put(JDK, jdkSerializer);
        serializers.put(FASTJSON, fastjsonSerializer);
        serializers.put(FASTJSON2, fastjson2Serializer);
        serializers.put(JACKSON_JSON, jacksonJsonSerializer);
        serializers.put(JACKSON_SMILE, jacksonSmileSerializer);
        serializers.put(FST, fstSerializer);
        serializers.put(FST_UNSAFE, fstUnsafeSerializer);
        serializers.put(KRYO, kryoSerializer);
        serializers.put(MESSAGE_PACK, messagePackSerializer);
        serializers.put(PROTOSTUFF, protoStuffSerializer);
        serializers.put(PROTOBUF, protobufSerializer);

    }

    protected void initUsers(String jsonPath, Class<?> userClass) throws IOException {
        this.userClass = userClass;
        if (userClass.equals(FareSearchResponse.class)) {
            FareSearchResponse fsr = buildFSR(jsonPath);
            users = Collections.singletonList(fsr);
        } else {
                try (InputStream is = AbstractState.class.getResourceAsStream(jsonPath)) {
                CollectionType type = MAPPER.getTypeFactory().constructCollectionType(List.class, userClass);
                users = MAPPER.readValue(is, type);
            }
        }
    }

    private FareSearchResponse buildFSR(String jsonPath) throws IOException {
        FareSearchResponse fsr;
        // read the file from resource
        String content = readFileToString(jsonPath);
        // using fastJson to generate the FareSearchResponse POJO object
        fsr = JSON.parseObject(content, FareSearchResponse.class);
        // using fastJson to print out an Object into a JSON text
        String json = JSON.toJSONString(fsr);
        return fsr;
    }

    protected void initSerializedBytesByType() {
        Stream.of(SerializationType.values()).forEach(type ->
            usersBytesBySerializationType.put(type, users.stream().map(user -> {
                try {
                    return serializers.get(type).serialize(user);
                } catch (Exception e) {
                    return null;
                }
            }).collect(Collectors.toList()))
        );
    }

    public void displaySerializedBytesByType() throws Exception {
        System.out.println(JDK + " serialize length = " + serialize(JDK));
        System.out.println(KRYO + " serialize length = " + serialize(KRYO));
        System.out.println(PROTOSTUFF + " serialize length = " + serialize(PROTOSTUFF));
        System.out.println(FASTJSON2 + " serialize length = " + serialize(FASTJSON2));
        System.out.println(PROTOBUF + " serialize length = " + serialize(PROTOBUF));
    }

    public long serialize(SerializationType serializationType) throws Exception {
        long serializationBytes = 0;
        for (Serializable user: users) {
            byte[] bytes = serializers.get(serializationType).serialize(user);
            serializationBytes += bytes.length;
        }
        return serializationBytes;
    }

    public void deserialize(SerializationType serializationType) throws Exception {
        List<byte[]> usersBytes = usersBytesBySerializationType.get(serializationType);
        for (byte[] usersByte : usersBytes) {
            Object deserializeUser = serializers.get(serializationType).deserialize(usersByte, userClass);
//            Serializable user = users.get(i);
//            String userStr = JSON.toJSONString(user);
//            String deserializeUserStr = JSON.toJSONString(deserializeUser);
//            JSONObject userObject = JSON.parseObject(userStr);
//            JSONObject deserializeUserObject = JSON.parseObject(deserializeUserStr);
//            if (!userObject.equals(deserializeUserObject)) {
//                System.out.println(serializationType + " serialization failed.");
//                throw new RuntimeException("Serialization failed: users are not equals after deserialization process.");
//            }
        }
    }
}
