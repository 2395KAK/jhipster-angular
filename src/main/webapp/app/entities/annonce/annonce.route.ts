import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Annonce } from 'app/shared/model/annonce.model';
import { AnnonceService } from './annonce.service';
import { AnnonceComponent } from './annonce.component';
import { AnnonceDetailComponent } from './annonce-detail.component';
import { AnnonceUpdateComponent } from './annonce-update.component';
import { AnnonceDeletePopupComponent } from './annonce-delete-dialog.component';
import { IAnnonce } from 'app/shared/model/annonce.model';

@Injectable({ providedIn: 'root' })
export class AnnonceResolve implements Resolve<IAnnonce> {
    constructor(private service: AnnonceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((annonce: HttpResponse<Annonce>) => annonce.body));
        }
        return of(new Annonce());
    }
}

export const annonceRoute: Routes = [
    {
        path: 'annonce',
        component: AnnonceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lbchApp.annonce.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'annonce/:id/view',
        component: AnnonceDetailComponent,
        resolve: {
            annonce: AnnonceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lbchApp.annonce.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'annonce/new',
        component: AnnonceUpdateComponent,
        resolve: {
            annonce: AnnonceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lbchApp.annonce.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'annonce/:id/edit',
        component: AnnonceUpdateComponent,
        resolve: {
            annonce: AnnonceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lbchApp.annonce.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const annoncePopupRoute: Routes = [
    {
        path: 'annonce/:id/delete',
        component: AnnonceDeletePopupComponent,
        resolve: {
            annonce: AnnonceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lbchApp.annonce.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
