import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { YorumService } from '../service/yorum.service';

import { YorumComponent } from './yorum.component';

describe('Yorum Management Component', () => {
  let comp: YorumComponent;
  let fixture: ComponentFixture<YorumComponent>;
  let service: YorumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [YorumComponent],
    })
      .overrideTemplate(YorumComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(YorumComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(YorumService);

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
    expect(comp.yorums?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
