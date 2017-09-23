import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TodoComponent } from './todo.component';
import { TodoDetailComponent } from './todo-detail.component';
import { TodoPopupComponent } from './todo-dialog.component';
import { TodoDeletePopupComponent } from './todo-delete-dialog.component';

@Injectable()
export class TodoResolvePagingParams implements Resolve<any> {

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

export const todoRoute: Routes = [
    {
        path: 'todo',
        component: TodoComponent,
        resolve: {
            'pagingParams': TodoResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Todos'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'todo/:id',
        component: TodoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Todos'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const todoPopupRoute: Routes = [
    {
        path: 'todo-new',
        component: TodoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Todos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'todo/:id/edit',
        component: TodoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Todos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'todo/:id/delete',
        component: TodoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Todos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
