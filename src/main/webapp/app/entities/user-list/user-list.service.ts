import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { UserList } from './user-list.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class UserListService {

    private resourceUrl = SERVER_API_URL + 'api/user-lists';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/user-lists';

    constructor(private http: Http) { }

    create(userList: UserList): Observable<UserList> {
        const copy = this.convert(userList);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(userList: UserList): Observable<UserList> {
        const copy = this.convert(userList);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<UserList> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(userList: UserList): UserList {
        const copy: UserList = Object.assign({}, userList);
        return copy;
    }
}
