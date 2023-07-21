package com.jedivision.temple.serialization;

import io.fury.Fury;
import io.fury.Language;
import org.springframework.stereotype.Service;

@Service
public class FuryUnsafeSerializer implements AbstractSerializer{
    private final Fury fury;
    public FuryUnsafeSerializer() {
        fury = Fury.builder().withLanguage(Language.XLANG).withRefTracking(false).disableSecureMode().build();
    }

    @Override
    public byte[] serialize(Object object) throws Exception {
        return fury.serialize(object);
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> type) throws Exception {
        return fury.deserialize(bytes);
    }
}
