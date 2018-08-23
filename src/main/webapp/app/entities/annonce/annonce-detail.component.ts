import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnonce } from 'app/shared/model/annonce.model';

@Component({
    selector: 'jhi-annonce-detail',
    templateUrl: './annonce-detail.component.html'
})
export class AnnonceDetailComponent implements OnInit {
    annonce: IAnnonce;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annonce }) => {
            this.annonce = annonce;
        });
    }

    previousState() {
        window.history.back();
    }
}
