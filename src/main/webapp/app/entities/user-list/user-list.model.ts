import { BaseEntity } from './../../shared';

export class UserList implements BaseEntity {
    constructor(
        public id?: number,
        public main?: boolean,
        public userId?: number,
        public todoListId?: number,
    ) {
        this.main = false;
    }
}
