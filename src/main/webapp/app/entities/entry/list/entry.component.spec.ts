import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EntryService } from '../service/entry.service';

import { EntryComponent } from './entry.component';

describe('Entry Management Component', () => {
  let comp: EntryComponent;
  let fixture: ComponentFixture<EntryComponent>;
  let service: EntryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EntryComponent],
    })
      .overrideTemplate(EntryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EntryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EntryService);

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
    expect(comp.entries?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
