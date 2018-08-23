import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IAnnonce } from 'app/shared/model/annonce.model';
import { AnnonceService } from './annonce.service';

@Component({
    selector: 'jhi-annonce-update',
    templateUrl: './annonce-update.component.html'
})
export class AnnonceUpdateComponent implements OnInit {
    private _annonce: IAnnonce;
    isSaving: boolean;

    constructor(private annonceService: AnnonceService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ annonce }) => {
            this.annonce = annonce;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.annonce.id !== undefined) {
            this.subscribeToSaveResponse(this.annonceService.update(this.annonce));
        } else {
            this.subscribeToSaveResponse(this.annonceService.create(this.annonce));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAnnonce>>) {
        result.subscribe((res: HttpResponse<IAnnonce>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get annonce() {
        return this._annonce;
    }

    set annonce(annonce: IAnnonce) {
        this._annonce = annonce;
    }
}
