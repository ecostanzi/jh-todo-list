import { BaseEntity } from './../../shared';

export class Todo implements BaseEntity {
    constructor(
        public id?: number,
        public text?: string,
        public authorId?: number,
    ) {
    }
}
