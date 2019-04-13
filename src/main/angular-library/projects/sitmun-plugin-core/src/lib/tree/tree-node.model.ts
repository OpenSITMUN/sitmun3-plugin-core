import {Resource} from 'angular-hal';
import {Cartography} from '../cartography/cartography.model';
import {Tree} from './tree.model';
export class TreeNode extends Resource {

  public name: string;
  public tooltip: string; 
  public orden : number;
  public active: boolean;
  public parent: TreeNode;

  public cartography: Cartography;
    
  public tree: Tree;

}
