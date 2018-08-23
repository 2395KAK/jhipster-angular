/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LbchTestModule } from '../../../test.module';
import { AnnonceUpdateComponent } from 'app/entities/annonce/annonce-update.component';
import { AnnonceService } from 'app/entities/annonce/annonce.service';
import { Annonce } from 'app/shared/model/annonce.model';

describe('Component Tests', () => {
    describe('Annonce Management Update Component', () => {
        let comp: AnnonceUpdateComponent;
        let fixture: ComponentFixture<AnnonceUpdateComponent>;
        let service: AnnonceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LbchTestModule],
                declarations: [AnnonceUpdateComponent]
            })
                .overrideTemplate(AnnonceUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AnnonceUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnnonceService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Annonce(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.annonce = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Annonce();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.annonce = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
