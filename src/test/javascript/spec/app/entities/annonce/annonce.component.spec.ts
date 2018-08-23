/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LbchTestModule } from '../../../test.module';
import { AnnonceComponent } from 'app/entities/annonce/annonce.component';
import { AnnonceService } from 'app/entities/annonce/annonce.service';
import { Annonce } from 'app/shared/model/annonce.model';

describe('Component Tests', () => {
    describe('Annonce Management Component', () => {
        let comp: AnnonceComponent;
        let fixture: ComponentFixture<AnnonceComponent>;
        let service: AnnonceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LbchTestModule],
                declarations: [AnnonceComponent],
                providers: []
            })
                .overrideTemplate(AnnonceComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AnnonceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnnonceService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Annonce(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.annonces[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
