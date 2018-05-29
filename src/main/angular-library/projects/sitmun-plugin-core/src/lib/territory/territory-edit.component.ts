import { Resource } from 'angular-hal';
import { ResourceHelper } from 'angular-hal'; 
import { TerritoryType } from './territory-type.model';
import { TerritoryTypeService } from './territory-type.service';
import { Territory } from './territory.model';
import {TerritoryService} from './territory.service';
import {Component, OnInit, OnDestroy} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs-compat';
import {MatTableDataSource} from '@angular/material';
import {SelectionModel} from '@angular/cdk/collections';


@Component({
  selector: 'app-territory-edit',
  templateUrl: './territory-edit.component.html',
  styleUrls: ['./territory-edit.component.css']
})
export class TerritoryEditComponent implements OnInit, OnDestroy {
  territory: Territory = new Territory();
  territories: Territory[];
  territoryTypes: TerritoryType[] = new Array<TerritoryType>();

  sub: Subscription;
  
  displayedColumns = ['select', 'name', 'scope','blocked'];

  selection = new SelectionModel<Territory>(true, []);
  
  dataSource = new MatTableDataSource<Territory>([]);


  constructor(private route: ActivatedRoute,
    private router: Router,
    private territoryService: TerritoryService,
    private territoryTypeService: TerritoryTypeService) {
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      this.territory.type = new TerritoryType();
      //this.territory.type._links.self.href = null;
      //this.territory.members = new Array<Territory>();
      this.getAllTerritoryTypes();
      this.getAllTerritories();
    

      if (id) {
        this.territoryService.get(id).subscribe((territory: any) => {
          if (territory) {
            this.territory = territory;
            this.territory.createdDate = new Date();
            this.territory.createdDate.setTime(Date.parse(territory.createdDate));
            
            //ResourceHelper.resolveRelations(this.territory);
            //alert('llego');
            
            this.territory.getRelation(TerritoryType, 'type').subscribe(
                    (tpe: TerritoryType) => this.territory.type = tpe,
                    error => this.territory.type = new TerritoryType());
            //
            this.territory.getRelationArray(Territory, 'members').subscribe(
                    (members: Territory[]) => {
                      
                    this.territory.members = members;
                    //this.selection = new SelectionModel<Territory>(true, this.territory.members);
                    this.dataSource.data.forEach(row => {
                        for (let member of this.territory.members){
                          if (row._links.self.href == member._links.self.href)
                             this.selection.select(row)
                        }
                      });

                 },
                    error => this.territory.members = new Array<Territory>());
            
            
            
            
          } else {
            console.log(`territory with id '${id}' not found, returning to list`);
            this.gotoList();
          }
        });
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
  
  getAllTerritories() {
    this.territoryService.getAll()
    .subscribe((territories: Territory[]) => {
        this.territories = territories;
        this.dataSource = new MatTableDataSource<Territory>(this.territories);
    });
  }
  
  getAllTerritoryTypes() {
    this.territoryTypeService.getAll()
    .subscribe((territoryTypes: TerritoryType[]) => {
        this.territoryTypes = territoryTypes;
    });
  }
  

  gotoList() {
    this.router.navigate(['/territory-list']);
  }

  save() {
    if (this.territory.createdDate != null && (typeof this.territory.createdDate != 'string')) {
      this.territory.createdDate = this.territory.createdDate.toISOString();
    }
    const isNew  = this.territory._links == null;
    
    if (isNew) {
       const territoryType = this.territory.type;
       this.territory.type = null;
       this.territoryService.save(this.territory).subscribe(result => { 
       this.territory['_links'] = result['_links'];
       if (territoryType !=null){
          this.territory.substituteRelation('type',territoryType).subscribe(result => {
      
      }, error => console.error(error));
      if (this.selection.selected!=null){
        for (var i=0; i< this.selection.selected.length; i++){
          this.territory.addRelation('members',this.selection.selected[i]).subscribe(result => {      
          }, error => console.error(error));
        }
      }
      this.gotoList();
         
    }
         
       }     
      
    , error => console.error(error));
      
    }
    
    
    if (!isNew) {
    //borro todas las relaciones que hubiera y añado las seleccionadas
    if (this.territory.members!=null){
      for (var i=0; i< this.territory.members.length; i++){
        this.territory.deleteRelation('members',this.territory.members[i]).subscribe(result => {      
        }, error => console.error(error));
      }
    }
    if (this.territory.type !=null){
          this.territory.substituteRelation('type',this.territory.type).subscribe(result => {
      
      }, error => console.error(error));
    }
    if (this.selection.selected!=null){
      for (var i=0; i< this.selection.selected.length; i++){
        this.territory.addRelation('members',this.selection.selected[i]).subscribe(result => {      
        }, error => console.error(error));
      }
    }

    
      delete this.territory.type;
      delete this.territory.members;

      this.territoryService.save(this.territory).subscribe(result => {      
        this.gotoList();
      }, error => console.error(error));
    } 
      
    


  }

  remove(territory: Territory) {
    this.territoryService.delete(territory).subscribe(result => {
      this.gotoList();
    }, error => console.error(error));

  }
  
  compareResource(c1: Resource, c2: Resource): boolean {
    return c1 && c2 ? c1._links.self.href === c2._links.self.href : c1 === c2;
}
  

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
        this.selection.clear() :
        this.dataSource.data.forEach(row => this.selection.select(row));
  }
}