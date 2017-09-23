import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JmintSharedModule } from '../../shared';
import { JmintAdminModule } from '../../admin/admin.module';
import {
    TodoListService,
    TodoListPopupService,
    TodoListComponent,
    TodoListDetailComponent,
    TodoListDialogComponent,
    TodoListPopupComponent,
    TodoListDeletePopupComponent,
    TodoListDeleteDialogComponent,
    todoListRoute,
    todoListPopupRoute,
    TodoListResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...todoListRoute,
    ...todoListPopupRoute,
];

@NgModule({
    imports: [
        JmintSharedModule,
        JmintAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TodoListComponent,
        TodoListDetailComponent,
        TodoListDialogComponent,
        TodoListDeleteDialogComponent,
        TodoListPopupComponent,
        TodoListDeletePopupComponent,
    ],
    entryComponents: [
        TodoListComponent,
        TodoListDialogComponent,
        TodoListPopupComponent,
        TodoListDeleteDialogComponent,
        TodoListDeletePopupComponent,
    ],
    providers: [
        TodoListService,
        TodoListPopupService,
        TodoListResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmintTodoListModule {}
