import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MufredatService } from '../service/mufredat.service';

import { MufredatComponent } from './mufredat.component';

describe('Mufredat Management Component', () => {
  let comp: MufredatComponent;
  let fixture: ComponentFixture<MufredatComponent>;
  let service: MufredatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MufredatComponent],
    })
      .overrideTemplate(MufredatComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MufredatComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MufredatService);

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
    expect(comp.mufredats?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
