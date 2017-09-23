import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TodoListComponent } from './todo-list.component';
import { TodoListDetailComponent } from './todo-list-detail.component';
import { TodoListPopupComponent } from './todo-list-dialog.component';
import { TodoListDeletePopupComponent } from './todo-list-delete-dialog.component';

@Injectable()
export class TodoListResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const todoListRoute: Routes = [
    {
        path: 'todo-list',
        component: TodoListComponent,
        resolve: {
            'pagingParams': TodoListResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TodoLists'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'todo-list/:id',
        component: TodoListDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TodoLists'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const todoListPopupRoute: Routes = [
    {
        path: 'todo-list-new',
        component: TodoListPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TodoLists'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'todo-list/:id/edit',
        component: TodoListPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TodoLists'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'todo-list/:id/delete',
        component: TodoListDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TodoLists'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
