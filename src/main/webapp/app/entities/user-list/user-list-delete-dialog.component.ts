import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { UserList } from './user-list.model';
import { UserListPopupService } from './user-list-popup.service';
import { UserListService } from './user-list.service';

@Component({
    selector: 'jhi-user-list-delete-dialog',
    templateUrl: './user-list-delete-dialog.component.html'
})
export class UserListDeleteDialogComponent {

    userList: UserList;

    constructor(
        private userListService: UserListService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userListService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'userListListModification',
                content: 'Deleted an userList'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-list-delete-popup',
    template: ''
})
export class UserListDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userListPopupService: UserListPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.userListPopupService
                .open(UserListDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
