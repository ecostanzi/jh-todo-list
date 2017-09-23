/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmintTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TodoListDetailComponent } from '../../../../../../main/webapp/app/entities/todo-list/todo-list-detail.component';
import { TodoListService } from '../../../../../../main/webapp/app/entities/todo-list/todo-list.service';
import { TodoList } from '../../../../../../main/webapp/app/entities/todo-list/todo-list.model';

describe('Component Tests', () => {

    describe('TodoList Management Detail Component', () => {
        let comp: TodoListDetailComponent;
        let fixture: ComponentFixture<TodoListDetailComponent>;
        let service: TodoListService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmintTestModule],
                declarations: [TodoListDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TodoListService,
                    JhiEventManager
                ]
            }).overrideTemplate(TodoListDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TodoListDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TodoListService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TodoList(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.todoList).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
