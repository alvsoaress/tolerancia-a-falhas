import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Trend, Rate } from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";


export const successfulRequests = new Counter('successful_requests');
export const failedRequests = new Counter('failed_requests');
export const responseTimes = new Trend('response_times');
export const requestSuccessRate = new Rate('request_success_rate');


export const options = {
    scenarios: {
        cargaRPS: {
            executor: 'ramping-arrival-rate', 
            startRate: 5,      
            timeUnit: '1s',
            stages: [
                { target: 10, duration: '10s' },   
                { target: 20, duration: '10s' },   
                { target: 40, duration: '10s' },   
                { target: 60, duration: '10s' },   
                { target: 80, duration: '10s' },   
                { target: 100, duration: '10s' },  
                { target: 150, duration: '10s' },  
                { target: 200, duration: '10s' },  
            ],
            preAllocatedVUs: 200,  
            maxVUs: 300,
        },
    },
};


export default function () {
    const url = 'url_da_airlinehub_aqui';
    const res = http.post(url);

    const isSuccess = res.status >= 200 && res.status < 300;

    check(res, {
        'status é 200-299': () => isSuccess,
    });

    if (isSuccess) {
        successfulRequests.add(1);
        requestSuccessRate.add(true);
    } else {
        failedRequests.add(1);
        requestSuccessRate.add(false);
    }

    responseTimes.add(res.timings.duration);


}

export function handleSummary(data) {
    return {
        "index.html": htmlReport(data),
    };
}
