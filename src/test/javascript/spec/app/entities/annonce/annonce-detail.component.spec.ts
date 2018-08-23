/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LbchTestModule } from '../../../test.module';
import { AnnonceDetailComponent } from 'app/entities/annonce/annonce-detail.component';
import { Annonce } from 'app/shared/model/annonce.model';

describe('Component Tests', () => {
    describe('Annonce Management Detail Component', () => {
        let comp: AnnonceDetailComponent;
        let fixture: ComponentFixture<AnnonceDetailComponent>;
        const route = ({ data: of({ annonce: new Annonce(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LbchTestModule],
                declarations: [AnnonceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AnnonceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnnonceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.annonce).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
