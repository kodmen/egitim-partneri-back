import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OgretmenService } from '../service/ogretmen.service';

import { OgretmenComponent } from './ogretmen.component';

describe('Ogretmen Management Component', () => {
  let comp: OgretmenComponent;
  let fixture: ComponentFixture<OgretmenComponent>;
  let service: OgretmenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OgretmenComponent],
    })
      .overrideTemplate(OgretmenComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OgretmenComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OgretmenService);

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
    expect(comp.ogretmen?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
