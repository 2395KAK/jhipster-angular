import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LbchSharedModule } from 'app/shared';
import {
    AnnonceComponent,
    AnnonceDetailComponent,
    AnnonceUpdateComponent,
    AnnonceDeletePopupComponent,
    AnnonceDeleteDialogComponent,
    annonceRoute,
    annoncePopupRoute
} from './';

const ENTITY_STATES = [...annonceRoute, ...annoncePopupRoute];

@NgModule({
    imports: [LbchSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AnnonceComponent,
        AnnonceDetailComponent,
        AnnonceUpdateComponent,
        AnnonceDeleteDialogComponent,
        AnnonceDeletePopupComponent
    ],
    entryComponents: [AnnonceComponent, AnnonceUpdateComponent, AnnonceDeleteDialogComponent, AnnonceDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LbchAnnonceModule {}
