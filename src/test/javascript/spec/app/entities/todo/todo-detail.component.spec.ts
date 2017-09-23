/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JmintTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TodoDetailComponent } from '../../../../../../main/webapp/app/entities/todo/todo-detail.component';
import { TodoService } from '../../../../../../main/webapp/app/entities/todo/todo.service';
import { Todo } from '../../../../../../main/webapp/app/entities/todo/todo.model';

describe('Component Tests', () => {

    describe('Todo Management Detail Component', () => {
        let comp: TodoDetailComponent;
        let fixture: ComponentFixture<TodoDetailComponent>;
        let service: TodoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JmintTestModule],
                declarations: [TodoDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TodoService,
                    JhiEventManager
                ]
            }).overrideTemplate(TodoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TodoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TodoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Todo(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.todo).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
