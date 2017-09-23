import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmintSharedModule } from '../../shared';
import { JmintAdminModule } from '../../admin/admin.module';
import {
    UserListService,
    UserListPopupService,
    UserListComponent,
    UserListDetailComponent,
    UserListDialogComponent,
    UserListPopupComponent,
    UserListDeletePopupComponent,
    UserListDeleteDialogComponent,
    userListRoute,
    userListPopupRoute,
} from './';

const ENTITY_STATES = [
    ...userListRoute,
    ...userListPopupRoute,
];

@NgModule({
    imports: [
        JmintSharedModule,
        JmintAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UserListComponent,
        UserListDetailComponent,
        UserListDialogComponent,
        UserListDeleteDialogComponent,
        UserListPopupComponent,
        UserListDeletePopupComponent,
    ],
    entryComponents: [
        UserListComponent,
        UserListDialogComponent,
        UserListPopupComponent,
        UserListDeleteDialogComponent,
        UserListDeletePopupComponent,
    ],
    providers: [
        UserListService,
        UserListPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmintUserListModule {}
