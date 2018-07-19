import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable} from 'rxjs-compat';
//import * as moment from 'moment';

@Injectable()
export class AuthService {
    
    
    public SERVER_API_URL = '/api';
    
    constructor(
        private http: HttpClient
    ) {}

    getToken() {

        return  sessionStorage.getItem('authenticationToken');
    }

    login(credentials): Observable<any> {

        const data = {
            username: credentials.username,
            password: credentials.password
        };
        return this.http.post(this.SERVER_API_URL + '/authenticate', data, {observe : 'response'}).map(authenticateSuccess.bind(this));

        function authenticateSuccess(resp) {
            const bearerToken = resp.headers.get('Authorization');
            if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
                const jwt = bearerToken.slice(7, bearerToken.length);
                this.storeAuthenticationToken(jwt);
                //const expiresAt = moment().add( resp.headers.get('Token-Validity'),'milisecond');
                //sessionStorage.setItem('expires_at', JSON.stringify(expiresAt.valueOf()));
                return jwt;
            }
            
            

            
        }
    }

    loginWithToken(jwt) {
        if (jwt) {
            this.storeAuthenticationToken(jwt);
            return Promise.resolve(jwt);
        } else {
            return Promise.reject('auth-jwt-service Promise reject'); // Put appropriate error message here
        }
    }

    storeAuthenticationToken(jwt) {
       sessionStorage.setItem('authenticationToken', jwt);
        
    }
    /*
    getExpiration() {
        const expiration = sessionStorage.getItem("expires_at");
        const expiresAt = JSON.parse(expiration);
        return expiresAt;
    } 
    */
    public isLoggedIn() {
        //return moment().isBefore(this.getExpiration());
        return this.getToken();
    }

    isLoggedOut() {
        return !this.isLoggedIn();
    }

    logout(): Observable<any> {

        return new Observable((observer) => {
            //localStorage.removeItem('authenticationToken');
            sessionStorage.removeItem('authenticationToken');
            //sessionStorage.removeItem('expires_at');
            observer.complete();
        });
    }


    
}
