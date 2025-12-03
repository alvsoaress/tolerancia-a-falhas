package com.tolerancia.falhas.imdtravel.service;

import com.tolerancia.falhas.imdtravel.dto.BuyTicketRequest;
import com.tolerancia.falhas.imdtravel.dto.BuyTicketResponse;
import com.tolerancia.falhas.imdtravel.dto.FlightInfo;
import com.tolerancia.falhas.imdtravel.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    
    @Autowired
    private AirlinesHubService airlinesHubService;
    
    @Autowired
    private ExchangeService exchangeService;
    
    @Autowired
    private FidelityService fidelityService;
    
    public BuyTicketResponse buyTicket(BuyTicketRequest request) {
        try {
            boolean ft = Boolean.TRUE.equals(request.getFt());

            FlightInfo flightInfo = airlinesHubService.getFlightInfoWithRetry(request.getFlight(), request.getDay(), ft);
            Double exchangeRate = exchangeService.getExchangeRateWithFallback(ft);
            Double convertedValue = flightInfo.getValue() * exchangeRate;
            TransactionResponse transactionResponse = airlinesHubService.sellTicketWithTimeout(request.getFlight(), request.getDay(), ft);
            fidelityService.addBonusNonBlocking(request.getUser(), convertedValue.intValue(), ft);
            
            return new BuyTicketResponse(
                true,
                transactionResponse.getId(),
                "Ticket purchased successfully"
            );
            
        } catch (Exception e) {
            return new BuyTicketResponse(
                false,
                null,
                "Error processing ticket purchase: " + e.getMessage()
            );
        }
    }
}
