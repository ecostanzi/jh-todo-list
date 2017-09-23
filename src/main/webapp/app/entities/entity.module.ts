import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JmintTodoModule } from './todo/todo.module';
import { JmintTodoListModule } from './todo-list/todo-list.module';
import { JmintUserListModule } from './user-list/user-list.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        JmintTodoModule,
        JmintTodoListModule,
        JmintUserListModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JmintEntityModule {}
