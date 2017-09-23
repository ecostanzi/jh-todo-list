import { BaseEntity } from './../../shared';

export class TodoList implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public authorId?: number,
    ) {
    }
}
