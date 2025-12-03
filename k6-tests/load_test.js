import http from 'k6/http';
import { check } from 'k6';
import { Counter, Trend, Rate } from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

export const successfulRequests = new Counter('successful_requests');
export const failedRequests = new Counter('failed_requests');
export const responseTimes = new Trend('response_times');
export const requestSuccessRate = new Rate('request_success_rate');


export const options = {

    scenarios: {
        carga_rps: {
            executor: 'ramping-arrival-rate', 
            startRate: 5,                   
            timeUnit: '1s',

            stages: [
                { target: 20,  duration: '30s' },  
                { target: 50,  duration: '30s' },  
                { target: 150, duration: '45s' },  // carga média
                { target: 300, duration: '45s' },  // carga alta
                { target: 0,   duration: '20s' },  // rampa para zero
            ],

            preAllocatedVUs: 50,   
            maxVUs: 500,           
        },
    },

    thresholds: {
        http_req_duration: ['p(95)<3000'],       
        request_success_rate: ['rate>0.95'],      
        failed_requests: ['count<50'],            
    },
};

// Função principal
export default function () {
    const url = 'url_da_airlinehub_aqui';

    const res = http.get(url);

    const ok = res.status >= 200 && res.status < 300;

    check(res, {
        'status 200-299': () => ok,
    });

    if (ok) {
        successfulRequests.add(1);
        requestSuccessRate.add(true);
    } else {
        failedRequests.add(1);
        requestSuccessRate.add(false);
    }

    responseTimes.add(res.timings.duration);
}

// Relatório HTML
export function handleSummary(data) {
    return {
        "index.html": htmlReport(data),
    };
}
