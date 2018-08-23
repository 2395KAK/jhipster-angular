/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LbchTestModule } from '../../../test.module';
import { AnnonceDeleteDialogComponent } from 'app/entities/annonce/annonce-delete-dialog.component';
import { AnnonceService } from 'app/entities/annonce/annonce.service';

describe('Component Tests', () => {
    describe('Annonce Management Delete Component', () => {
        let comp: AnnonceDeleteDialogComponent;
        let fixture: ComponentFixture<AnnonceDeleteDialogComponent>;
        let service: AnnonceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LbchTestModule],
                declarations: [AnnonceDeleteDialogComponent]
            })
                .overrideTemplate(AnnonceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnnonceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnnonceService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
