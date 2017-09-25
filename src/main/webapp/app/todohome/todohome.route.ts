import { Route } from '@angular/router';

import {TodohomeComponent} from './todohome.component';

export const TODOHOME_ROUTE: Route = {
    path: 'todohome',
    component: TodohomeComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Todo Lists!'
    }
};
