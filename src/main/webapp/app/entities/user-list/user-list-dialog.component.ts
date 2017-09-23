import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { UserList } from './user-list.model';
import { UserListPopupService } from './user-list-popup.service';
import { UserListService } from './user-list.service';
import { User, UserService } from '../../shared';
import { TodoList, TodoListService } from '../todo-list';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-user-list-dialog',
    templateUrl: './user-list-dialog.component.html'
})
export class UserListDialogComponent implements OnInit {

    userList: UserList;
    isSaving: boolean;

    users: User[];

    todolists: TodoList[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private userListService: UserListService,
        private userService: UserService,
        private todoListService: TodoListService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.todoListService.query()
            .subscribe((res: ResponseWrapper) => { this.todolists = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.userList.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userListService.update(this.userList));
        } else {
            this.subscribeToSaveResponse(
                this.userListService.create(this.userList));
        }
    }

    private subscribeToSaveResponse(result: Observable<UserList>) {
        result.subscribe((res: UserList) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: UserList) {
        this.eventManager.broadcast({ name: 'userListListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackTodoListById(index: number, item: TodoList) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-user-list-popup',
    template: ''
})
export class UserListPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userListPopupService: UserListPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.userListPopupService
                    .open(UserListDialogComponent as Component, params['id']);
            } else {
                this.userListPopupService
                    .open(UserListDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
