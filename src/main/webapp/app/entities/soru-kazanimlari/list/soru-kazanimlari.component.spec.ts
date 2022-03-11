import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SoruKazanimlariService } from '../service/soru-kazanimlari.service';

import { SoruKazanimlariComponent } from './soru-kazanimlari.component';

describe('SoruKazanimlari Management Component', () => {
  let comp: SoruKazanimlariComponent;
  let fixture: ComponentFixture<SoruKazanimlariComponent>;
  let service: SoruKazanimlariService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SoruKazanimlariComponent],
    })
      .overrideTemplate(SoruKazanimlariComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoruKazanimlariComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SoruKazanimlariService);

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
    expect(comp.soruKazanimlaris?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
