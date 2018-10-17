import { Resource } from 'angular-hal';
import { ResourceHelper } from 'angular-hal';
import { Tree } from './tree.model';
import {TreeService} from './tree.service';
import { Role } from '../role/role.model';
import { RoleService } from '../role/role.service';
import {Component, OnInit, OnDestroy, ChangeDetectorRef} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription, Observable, forkJoin, merge, concat, pipe, from} from 'rxjs';
import {MatTableDataSource} from '@angular/material';
import {SelectionModel} from '@angular/cdk/collections';


@Component({
    selector: 'sitmun-tree-edit',
    templateUrl: './tree-edit.component.html',
    styleUrls: ['./tree-edit.component.css']
})
export class TreeEditComponent implements OnInit, OnDestroy {
    tree: Tree = new Tree();
    roles: Role[] = new Array<Role>();
    sub: Subscription;

    displayedColumns = ['select', 'name'];

    roleSelection = new SelectionModel<Role>(true, []);

    roleDataSource = new MatTableDataSource<Role>([]);


    constructor(private route: ActivatedRoute,
        private router: Router,
        private roleService: RoleService,
        private changeDetectorRefs: ChangeDetectorRef,
        private treeService: TreeService) {


    }

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            const id = params['id'];

            if (id) {
                this.treeService.get(id).subscribe((tree: any) => {
                    if (tree) {
                        this.tree = tree;
                        this.roleService.getAll()
                            .subscribe((roles: Role[]) => {
                                this.roles = roles;
                                this.roleDataSource = new MatTableDataSource<Role>(this.roles);
                                this.tree.getRelationArray(Role, 'availableRoles').subscribe(
                                    (roles: Role[]) => {

                                        this.tree.availableRoles = roles;
                                        this.roleDataSource.data.forEach(row => {
                                            for (let member of this.tree.availableRoles) {
                                                if (row._links.self.href == member._links.self.href)
                                                    this.roleSelection.select(row);
                                            }
                                        });
                                        this.changeDetectorRefs.detectChanges();

                                    },
                                    error => this.tree.availableRoles = new Array<Role>());


                            });





                    } else {
                        console.log(`tree with id '${id}' not found, returning to list`);
                        this.gotoList();
                    }
                });
            } else {
                this.getAllRoles();
            }
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    getAllRoles() {
        this.roleService.getAll()
            .subscribe((roles: Role[]) => {
                this.roles = roles;
                this.roleDataSource = new MatTableDataSource<Role>(this.roles);


            });
    }



    gotoList() {
        this.router.navigate(['/tree-list']);
    }

    save() {
        const isNew = this.tree._links == null;

        if (isNew) {
            if (this.roleSelection.selected != null) {
                this.tree.availableRoles = this.roleSelection.selected;
            }

            this.tree.availableRoles = this.tree.availableRoles.map(function(role) {
                return role._links.self.href;
            });

            this.treeService.create(this.tree).subscribe(result => {
                this.gotoList();

            }

                , error => console.error(error));

        } else {

            let rolesUpdate = null;
            let treeRoles = this.tree.availableRoles;
            if (treeRoles)
                rolesUpdate = concat(forkJoin(treeRoles.map(role => this.tree.deleteRelation('availableRoles', role))));
            if (this.roleSelection.selected) {
                if (rolesUpdate != null)
                    rolesUpdate = concat(rolesUpdate, forkJoin(this.roleSelection.selected.map(role => this.tree.addRelation('availableRoles', role))));
                else
                    rolesUpdate = concat(forkJoin(this.roleSelection.selected.map(role => this.tree.addRelation('availableRoles', role))));
            }


            rolesUpdate.subscribe(result => {

            }
                , error => console.error(error));

            delete this.tree.availableRoles;
            let update = concat(
                this.treeService.update(this.tree)


            );



            update.subscribe(result => {
                this.gotoList();
            }
                , error => console.error(error));


        }




    }

    remove(tree: Tree) {
        this.treeService.delete(tree).subscribe(result => {
            this.gotoList();
        }, error => console.error(error));

    }

    compareResource(c1: Resource, c2: Resource): boolean {
        return c1 && c2 ? c1._links.self.href === c2._links.self.href : c1 === c2;
    }

    isAllRoleSelected() {
        const numSelected = this.roleSelection.selected.length;
        const numRows = this.roleDataSource.data.length;
        return numSelected === numRows;
    }

    masterToggleRole() {
        this.isAllRoleSelected() ?
            this.roleSelection.clear() :
            this.roleDataSource.data.forEach(row => this.roleSelection.select(row));
    }



}