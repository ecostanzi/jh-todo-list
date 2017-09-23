import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserListComponent } from './user-list.component';
import { UserListDetailComponent } from './user-list-detail.component';
import { UserListPopupComponent } from './user-list-dialog.component';
import { UserListDeletePopupComponent } from './user-list-delete-dialog.component';

export const userListRoute: Routes = [
    {
        path: 'user-list',
        component: UserListComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserLists'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-list/:id',
        component: UserListDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserLists'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userListPopupRoute: Routes = [
    {
        path: 'user-list-new',
        component: UserListPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserLists'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-list/:id/edit',
        component: UserListPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserLists'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-list/:id/delete',
        component: UserListDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserLists'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
