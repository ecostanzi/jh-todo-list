/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmintTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { UserListDetailComponent } from '../../../../../../main/webapp/app/entities/user-list/user-list-detail.component';
import { UserListService } from '../../../../../../main/webapp/app/entities/user-list/user-list.service';
import { UserList } from '../../../../../../main/webapp/app/entities/user-list/user-list.model';

describe('Component Tests', () => {

    describe('UserList Management Detail Component', () => {
        let comp: UserListDetailComponent;
        let fixture: ComponentFixture<UserListDetailComponent>;
        let service: UserListService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmintTestModule],
                declarations: [UserListDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    UserListService,
                    JhiEventManager
                ]
            }).overrideTemplate(UserListDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserListDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserListService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new UserList(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.userList).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
