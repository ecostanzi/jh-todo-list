import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TodoList } from './todo-list.model';
import { TodoListService } from './todo-list.service';

@Component({
    selector: 'jhi-todo-list-detail',
    templateUrl: './todo-list-detail.component.html'
})
export class TodoListDetailComponent implements OnInit, OnDestroy {

    todoList: TodoList;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private todoListService: TodoListService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTodoLists();
    }

    load(id) {
        this.todoListService.find(id).subscribe((todoList) => {
            this.todoList = todoList;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTodoLists() {
        this.eventSubscriber = this.eventManager.subscribe(
            'todoListListModification',
            (response) => this.load(this.todoList.id)
        );
    }
}
