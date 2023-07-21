package com.jedivision.temple.serialization;

import com.jedivision.temple.entity.*;
import com.jedivision.temple.entity.pojo.*;
import io.fury.Fury;
import io.fury.Language;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class FurySerializer implements AbstractSerializer{
    private final Fury fury;

    public FurySerializer() {
        fury = Fury.builder().withLanguage(Language.XLANG).withRefTracking(false).build();
        fury.register(AgentPrice.class);
        fury.register(Availabilities.class);
        fury.register(Baggage.class);
        fury.register(Date.class);
        fury.register(Body.class);
        fury.register(FareComponents.class);
        fury.register(FareInfos.class);
        fury.register(FareSearchResponse.class);
        fury.register(FlightFareMappings.class);
        fury.register(FlightRefCombination.class);
        fury.register(Flights.class);
        fury.register(Head.class);
        fury.register(MiniRule.class);
        fury.register(OptionalKVs.class);
        fury.register(PassengerFares.class);
        fury.register(PassengerRestriction.class);
        fury.register(Penalties.class);
        fury.register(PieceAllowance.class);
        fury.register(PriceAttributes.class);
        fury.register(PricingList.class);
        fury.register(SaleControlInfo.class);
        fury.register(SeatClassRef.class);
        fury.register(SegmentBaggage.class);
        fury.register(ShoppingResults.class);
        fury.register(TicketLocationPart.class);
        fury.register(TicketLocations.class);
        fury.register(TicketRefs.class);
        fury.register(Tickets.class);
        fury.register(WeightAllowance.class);


        fury.register(ArrayList.class);
        fury.register(Gender.class);
        fury.register(Task.class);
        fury.register(Force.class);
        fury.register(Youngling.class);
        fury.register(Padawan.class);
        fury.register(Master.class);
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
