import {Resource} from 'angular-hal';
import {Cartography} from './cartography.model';
import {Role} from '../roles/role.model';
export class CartographyGroup extends Resource {

  public name: string;
  public type: string; 
  public members: Cartography[];
  public role: Role[];

}