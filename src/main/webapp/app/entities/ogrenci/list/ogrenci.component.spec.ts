import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OgrenciService } from '../service/ogrenci.service';

import { OgrenciComponent } from './ogrenci.component';

describe('Ogrenci Management Component', () => {
  let comp: OgrenciComponent;
  let fixture: ComponentFixture<OgrenciComponent>;
  let service: OgrenciService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OgrenciComponent],
    })
      .overrideTemplate(OgrenciComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OgrenciComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OgrenciService);

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
    expect(comp.ogrencis?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
