import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TodohomeComponent} from './todohome.component';
import {TODOHOME_ROUTE} from './todohome.route';
import {RouterModule} from '@angular/router';
import {JmintSharedModule} from '../shared/shared.module';
import {JmintEntityModule} from '../entities/entity.module';

@NgModule({
    imports: [
        CommonModule,
        JmintSharedModule,
        JmintEntityModule,
        RouterModule.forRoot([TODOHOME_ROUTE], {useHash: true})
    ],
    declarations: [TodohomeComponent]
})
export class TodohomeModule {
}
