import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RozetService } from '../service/rozet.service';

import { RozetComponent } from './rozet.component';

describe('Rozet Management Component', () => {
  let comp: RozetComponent;
  let fixture: ComponentFixture<RozetComponent>;
  let service: RozetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RozetComponent],
    })
      .overrideTemplate(RozetComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RozetComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RozetService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.rozets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
