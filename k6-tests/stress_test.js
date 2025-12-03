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
        stress_test: {
            executor: 'ramping-arrival-rate',
            startRate: 10,
            timeUnit: '1s',

            stages: [
                { target: 50,  duration: '1m' },    // carga leve
                { target: 100, duration: '1m' },    // carga moderada
                { target: 200, duration: '1m' },    // perto do limite
                { target: 400, duration: '2m' },    // carga alta
                { target: 600, duration: '2m' },    // estresse pesado
                { target: 800, duration: '2m' },    // ponto de ruptura
                { target: 0, duration: '30s' },     // cooldown
            ],

            preAllocatedVUs: 300,
            maxVUs: 1000,  
        },
    },

    thresholds: {
        http_req_duration: ['p(95)<5000'],    
        request_success_rate: ['rate>0.80'],  
        failed_requests: ['count<200'],       
    },
};

export default function () {
    const url = 'url_da_airlinehub_aqui';

    const res = http.post(url);
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
