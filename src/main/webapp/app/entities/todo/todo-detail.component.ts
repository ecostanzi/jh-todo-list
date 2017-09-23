import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Todo } from './todo.model';
import { TodoService } from './todo.service';

@Component({
    selector: 'jhi-todo-detail',
    templateUrl: './todo-detail.component.html'
})
export class TodoDetailComponent implements OnInit, OnDestroy {

    todo: Todo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private todoService: TodoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTodos();
    }

    load(id) {
        this.todoService.find(id).subscribe((todo) => {
            this.todo = todo;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTodos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'todoListModification',
            (response) => this.load(this.todo.id)
        );
    }
}
