import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TodoList } from './todo-list.model';
import { TodoListPopupService } from './todo-list-popup.service';
import { TodoListService } from './todo-list.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-todo-list-dialog',
    templateUrl: './todo-list-dialog.component.html'
})
export class TodoListDialogComponent implements OnInit {

    todoList: TodoList;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private todoListService: TodoListService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.todoList.id !== undefined) {
            this.subscribeToSaveResponse(
                this.todoListService.update(this.todoList));
        } else {
            this.subscribeToSaveResponse(
                this.todoListService.create(this.todoList));
        }
    }

    private subscribeToSaveResponse(result: Observable<TodoList>) {
        result.subscribe((res: TodoList) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TodoList) {
        this.eventManager.broadcast({ name: 'todoListListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-todo-list-popup',
    template: ''
})
export class TodoListPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private todoListPopupService: TodoListPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.todoListPopupService
                    .open(TodoListDialogComponent as Component, params['id']);
            } else {
                this.todoListPopupService
                    .open(TodoListDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
