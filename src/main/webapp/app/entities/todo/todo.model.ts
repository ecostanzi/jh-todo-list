import { BaseEntity } from './../../shared';

export class Todo implements BaseEntity {
    constructor(
        public id?: number,
        public text?: string,
        public done?: boolean,
        public createdDate?: any,
        public authorId?: number,
    ) {
        this.done = false;
    }
}
