package com.jedivision.temple.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
@State(Scope.Benchmark)
public class KryoSerializer implements AbstractSerializer {
    private final Kryo kryo = new Kryo();
    KryoSerializer() {
//        kryo.register(Date.class);
//        kryo.register(ArrayList.class);
//        kryo.register(Gender.class);
//        kryo.register(Task.class);
//        kryo.register(Force.class);
//        kryo.register(Youngling.class);
//        kryo.register(Padawan.class);
//        kryo.register(Master.proto.class);
        kryo.setRegistrationRequired(false);
    }

    @Override
    public byte[] serialize(Object object) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Output output = new Output(outputStream)) {
            kryo.writeObject(output, object);
            output.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> type) throws Exception {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            return kryo.readObject(input, type);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kryo.reset();
        }
        return null;
    }
}
