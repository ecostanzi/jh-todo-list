import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { UserList } from './user-list.model';
import { UserListService } from './user-list.service';

@Component({
    selector: 'jhi-user-list-detail',
    templateUrl: './user-list-detail.component.html'
})
export class UserListDetailComponent implements OnInit, OnDestroy {

    userList: UserList;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private userListService: UserListService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUserLists();
    }

    load(id) {
        this.userListService.find(id).subscribe((userList) => {
            this.userList = userList;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserLists() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userListListModification',
            (response) => this.load(this.userList.id)
        );
    }
}
