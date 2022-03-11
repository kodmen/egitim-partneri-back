import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BolumService } from '../service/bolum.service';

import { BolumComponent } from './bolum.component';

describe('Bolum Management Component', () => {
  let comp: BolumComponent;
  let fixture: ComponentFixture<BolumComponent>;
  let service: BolumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BolumComponent],
    })
      .overrideTemplate(BolumComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BolumComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BolumService);

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
    expect(comp.bolums?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
