import { ApplicationParameter } from './application-parameter.model';
import { Injectable, Injector } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import {RestService} from 'angular-hal';  

@Injectable() 
export class ApplicationParameterService extends RestService<ApplicationParameter> {
  
  public API = '/api';
  public APPLICATION_PARAMETER_API = this.API + '/application-parameters';


  constructor(injector: Injector,private http: HttpClient) {
    super(ApplicationParameter, "application-parameters", injector);
  }
  
  remove(item: ApplicationParameter) {
    return this.http.delete(item._links.self.href);
   
  }
  
  save(item: ApplicationParameter): Observable<any> {
    let result: Observable<Object>;
    if (item._links!=null) {
      result = this.http.put(item._links.self.href, item);
      if (item.application !=null){
          item.substituteRelation('application',item.application).subscribe(result => {
      
      }, error => console.error(error));
      }
      
    } else {
      item.application = item.application._links.self.href;
  
      result = this.http.post(this.APPLICATION_PARAMETER_API , item);
    }
    return result;
  }
  
}