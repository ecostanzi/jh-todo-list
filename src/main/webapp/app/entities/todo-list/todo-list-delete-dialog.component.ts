import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TodoList } from './todo-list.model';
import { TodoListPopupService } from './todo-list-popup.service';
import { TodoListService } from './todo-list.service';

@Component({
    selector: 'jhi-todo-list-delete-dialog',
    templateUrl: './todo-list-delete-dialog.component.html'
})
export class TodoListDeleteDialogComponent {

    todoList: TodoList;

    constructor(
        private todoListService: TodoListService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.todoListService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'todoListListModification',
                content: 'Deleted an todoList'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-todo-list-delete-popup',
    template: ''
})
export class TodoListDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private todoListPopupService: TodoListPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.todoListPopupService
                .open(TodoListDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
