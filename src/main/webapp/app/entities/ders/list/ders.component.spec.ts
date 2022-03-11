import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DersService } from '../service/ders.service';

import { DersComponent } from './ders.component';

describe('Ders Management Component', () => {
  let comp: DersComponent;
  let fixture: ComponentFixture<DersComponent>;
  let service: DersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DersComponent],
    })
      .overrideTemplate(DersComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DersComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DersService);

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
    expect(comp.ders?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
